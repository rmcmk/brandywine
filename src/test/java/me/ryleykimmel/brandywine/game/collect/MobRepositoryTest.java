package me.ryleykimmel.brandywine.game.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ryleykimmel.brandywine.game.model.Mob;

public final class MobRepositoryTest {

	// For testing purposes
	private static final AtomicInteger count = new AtomicInteger(0);

	private static final class MobStub extends Mob {
		private final int id = count.getAndIncrement();

		@Override
		public String toString() {
			return Integer.toString(id);
		}
	}

	private MobRepository<MobStub> repo;

	@Before
	public void setup() {
		repo = new MobRepository<>(10);
	}

	@Test
	public void test_capacity() {
		Assert.assertEquals(10, repo.capacity());
	}

	@Test
	public void test_add() {
		repo.add(new MobStub());
		Assert.assertEquals(1, repo.size());
	}

	@Test
	public void test_remove() {
		MobStub stub = new MobStub();

		repo.add(stub);
		Assert.assertEquals(1, repo.size());

		repo.remove(stub.getIndex());
		Assert.assertEquals(0, repo.size());

		repo.add(stub);
		Assert.assertEquals(1, repo.size());

		repo.remove(stub);
		Assert.assertEquals(0, repo.size());
	}

	@Test
	public void test_get() {
		MobStub stub = new MobStub();
		MobStub stub1 = new MobStub();

		repo.add(stub);
		repo.add(stub1);

		Assert.assertEquals(stub, repo.get(1));
		Assert.assertEquals(stub1, repo.get(2));
	}

	@Test
	public void test_iterator() {
		MobStub stub = new MobStub();
		MobStub stub1 = new MobStub();

		repo.add(stub);
		repo.add(stub1);

		Iterator<MobStub> it = repo.iterator();
		Assert.assertTrue(it.hasNext());

		Assert.assertEquals(stub, it.next());
		Assert.assertEquals(stub1, it.next());

		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testIteratorRemoveAll() {
		MobStub stub = new MobStub();
		MobStub stub1 = new MobStub();

		repo.add(stub);
		repo.add(stub1);

		Assert.assertEquals(2, repo.size());

		Iterator<MobStub> it = repo.iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}

		Assert.assertEquals(0, repo.size());
	}

	@Test
	public void test_iterator_has_next() {
		MobStub stub = new MobStub();
		MobStub stub1 = new MobStub();

		repo.add(stub);
		repo.add(stub1);

		Iterator<MobStub> it = repo.iterator();
		Assert.assertTrue(it.hasNext());

		it.next();
		it.remove();

		it.next();
		it.remove();

		Assert.assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void test_iterator_nsee_next() {
		MobStub stub = new MobStub();
		MobStub stub1 = new MobStub();

		repo.add(stub);
		repo.add(stub1);

		Iterator<MobStub> it = repo.iterator();
		it.next();
		it.next();
		it.next(); // no such element exception
	}

}
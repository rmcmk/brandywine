package me.ryleykimmel.brandywine.game.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ryleykimmel.brandywine.game.model.Mob;

public final class MobRepositoryTest {

  // TODO: Utilize mockito or some other testing aid to prevent code like this.
  private static final class MobStub extends Mob {

    public MobStub() {
      super(null, null);
    }

  }

  private MobRepository<MobStub> repo;

  @Before
  public void setup() {
    repo = new MobRepository<>(10);
  }

  @Test
  public void testCapacity() {
    Assert.assertEquals(10, repo.capacity());
  }

  @Test
  public void testAdd() {
    repo.add(new MobStub());
    Assert.assertEquals(1, repo.size());
  }

  @Test
  public void testRemove() {
    MobStub stub = new MobStub();

    repo.add(stub);
    Assert.assertEquals(1, repo.size());

    repo.remove(stub);
    Assert.assertEquals(0, repo.size());
  }

  @Test
  public void testGet() {
    MobStub stub = new MobStub();
    MobStub stub1 = new MobStub();

    repo.add(stub);
    repo.add(stub1);

    Assert.assertEquals(stub, repo.get(1));
    Assert.assertEquals(stub1, repo.get(2));
  }

  @Test
  public void testCreateIterator() {
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
  public void testIteratorHasNext() {
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
  public void testIteratorNextFailure() {
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

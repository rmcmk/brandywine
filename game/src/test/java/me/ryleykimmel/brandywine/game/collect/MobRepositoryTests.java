package me.ryleykimmel.brandywine.game.collect;

import static com.google.common.truth.Truth.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.model.player.Player;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests the {@link MobRepository} class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Player.class)
public final class MobRepositoryTests {

  /**
   * The default capacity of a {@link MobRepository}.
   */
  private static final int DEFAULT_CAPACITY = 10;

  /**
   * The expected exception rule.
   */
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  /**
   * Tests {@link MobRepository#capacity()}.
   */
  @Test
  public void capacity() {
    MobRepository<Player> repository = new MobRepository<>(DEFAULT_CAPACITY);
    assertThat(DEFAULT_CAPACITY).isEqualTo(repository.capacity());
  }

  /**
   * Tests {@link MobRepository#add(Mob)}.
   */
  @Test
  public void add() {
    MobRepository<Player> repository = new MobRepository<>(DEFAULT_CAPACITY);
    Player player = mock(Player.class);
    when(player.getIndex()).thenReturn(1);

    assertThat(0).isEqualTo(repository.size());
    assertThat(repository.add(player)).isTrue();
    assertThat(1).isEqualTo(repository.size());

    assertThat(player).isEqualTo(repository.get(player.getIndex()));
  }

  /**
   * Tests {@link MobRepository#remove(Mob)}.
   */
  @Test
  public void remove() {
    MobRepository<Player> repository = new MobRepository<>(DEFAULT_CAPACITY);
    Player player = mock(Player.class);
    when(player.getIndex()).thenReturn(1);

    assertThat(0).isEqualTo(repository.size());
    assertThat(repository.add(player)).isTrue();
    assertThat(1).isEqualTo(repository.size());
    repository.remove(player);
    assertThat(0).isEqualTo(repository.size());

    assertThat(repository.get(player.getIndex())).isNull();
  }

  /**
   * Tests {@link MobRepository#get(int)}.
   */
  @Test
  public void get() {
    MobRepository<Player> repository = new MobRepository<>(DEFAULT_CAPACITY);
    Player player = mock(Player.class);
    Player other = mock(Player.class);
    when(player.getIndex()).thenReturn(1);
    when(other.getIndex()).thenReturn(2);

    assertThat(repository.add(player)).isTrue();
    assertThat(repository.add(other)).isTrue();

    assertThat(repository.get(player.getIndex())).isEqualTo(player);
    assertThat(repository.get(other.getIndex())).isEqualTo(other);
  }

  /**
   * Tests {@link MobRepository#size()}.
   */
  @Test
  public void size() {
    MobRepository<Player> repository = new MobRepository<>(DEFAULT_CAPACITY);

    assertThat(repository.size()).isEqualTo(0);

    for (int index = 0; index < DEFAULT_CAPACITY; index++) {
      Player player = mock(Player.class);
      assertThat(repository.add(player)).isTrue();
    }

    assertThat(repository.size()).isEqualTo(DEFAULT_CAPACITY);
  }

}

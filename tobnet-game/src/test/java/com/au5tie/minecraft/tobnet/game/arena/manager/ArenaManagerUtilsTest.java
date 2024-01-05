package com.au5tie.minecraft.tobnet.game.arena.manager;

import static org.mockito.Mockito.mock;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ArenaManagerUtilsTest {

  private TobnetArena arena = mock(TobnetArena.class);
  private List<ArenaManager> managers = new ArrayList<>();

  @BeforeEach
  public void setup() {
    Mockito.when(arena.getManagers()).thenReturn(managers);
  }

  @DisplayName("Empty Managers - Not Found")
  @Test
  void shouldTestGetManagerFromEmptyManagers() {
    Assertions.assertFalse(
      ArenaManagerUtils
        .getManagerOfType(arena, ArenaManagerType.PLAYER)
        .isPresent()
    );
  }

  @DisplayName("Manager Matching Type - Found")
  @Test
  void shouldTestGetManagerMatchingTypeFound() {
    managers.add(new ArenaPlayerManager(arena));

    Assertions.assertTrue(
      ArenaManagerUtils
        .getManagerOfType(arena, ArenaManagerType.PLAYER)
        .isPresent()
    );
  }

  @DisplayName("Manager Matching Type - Not Found")
  @Test
  void shouldTestGetManagerMatchingTypeNotFound() {
    managers.add(new ArenaPlayerManager(arena));

    Assertions.assertFalse(
      ArenaManagerUtils
        .getManagerOfType(arena, ArenaManagerType.GAME)
        .isPresent()
    );
  }

  @DisplayName("Custom Manager - Not Found")
  @Test
  void shouldTestGetCustomArenaManagerNotFound() {
    managers.add(
      new CustomArenaManager("test", arena) {
        @Override
        public void prepareManager() {
          //
        }

        @Override
        public void destroyManager() {
          //
        }
      }
    );

    Assertions.assertFalse(
      ArenaManagerUtils.getCustomManager(arena, "example").isPresent()
    );
  }

  @DisplayName("Custom Manager - Found")
  @Test
  void shouldTestGetCustomArenaManagerFound() {
    managers.add(
      new CustomArenaManager("test", arena) {
        @Override
        public void prepareManager() {
          //
        }

        @Override
        public void destroyManager() {
          //
        }
      }
    );

    Assertions.assertTrue(
      ArenaManagerUtils.getCustomManager(arena, "test").isPresent()
    );
  }
}

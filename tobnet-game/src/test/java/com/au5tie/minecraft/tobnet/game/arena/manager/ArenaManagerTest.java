package com.au5tie.minecraft.tobnet.game.arena.manager;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@NoArgsConstructor
@Getter
public class ArenaManagerTest {

    @Mock
    private TobnetArena arena;

    private final List<ArenaManager> managers = new ArrayList<>();

    /**
     * This will register a mock {@link ArenaManager}. Since the managers in Tobnet dynamically link, this will mock
     * the discovery of the arena manager to the one provided.
     *
     * This is the first step in providing a manager to be mocked for use in manager tests. Once all of the managers have
     * been registered via this method, then you'll want to call completeMockArenaManagerRegistration() which will complete
     * the mocking process. Failure to call that method will result in the mocks not taking effect.
     *
     * @param mockManager Mock Arena Manager.
     * @param type The manager type.
     * @author au5tie
     */
    public void registerMockArenaManager(ArenaManager mockManager, ArenaManagerType type) {
        // Add the manager to our temporary list which will eventually be registered on the arena.
        managers.add(mockManager);

        // We have to mock the type return on this manager since that's how the dynamic linking finds them.
        Mockito.doReturn(type).when(mockManager).getType();
    }

    /**
     * This will complete the mocking of the managers on the arena which the provided {@link ArenaManager} will use for
     * dynamic linking. Once all of the mock managers have been registered by calling registerMockArenaManager(), then
     * this should be called as the second and final step. This will mock all of the registered managers and call the
     * current manager's post-setup phase which will perform the dynamic linking of arena managers. This is how the actual
     * manager's class level dynamic other-manager links will be populated for your tests.
     *
     * @param currentManager Arena Manager being tested.
     * @author au5tie
     */
    public void completeMockArenaManagerRegistration(ArenaManager currentManager) {
        // Mock the return of the arena manager get to our temporary list.e dynamic linking to our "temp" managers.
        Mockito.doReturn(managers).when(arena).getManagers();

        // Call the current manager's after prep call which will do the dynamic linking to our "temp" managers.
        currentManager.afterArenaPreparationComplete();
    }

    /**
     * Mocks the game managers status that it will return when asked. This is how you can easily mock the game manager to
     * return a pre-defined status for your tests.
     *
     * @param gameManager Arena Game Manager.
     * @param status Arena Game Status.
     * @author au5tie
     */
    public void registerArenaGameStatusMock(ArenaGameManager gameManager, ArenaGameStatus status) {

        Mockito.doReturn(status).when(gameManager).getGameStatus();
    }
}

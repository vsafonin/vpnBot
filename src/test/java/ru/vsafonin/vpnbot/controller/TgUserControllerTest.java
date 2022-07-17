package ru.vsafonin.vpnbot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.service.UserService;
import ru.vsafonin.vpnbot.service.UuidSerice;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TgUserController.class)
class TgUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UuidSerice uuidSerice;

    /**
     * test checks that we can get page for create user
     */
    @Test
    void getNewTgUser() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Mockito.when(uuidSerice.getTgUserByUuid(uuid)).thenReturn(new TgUser());
        mockMvc.perform(MockMvcRequestBuilders.get("/newTgUser/" + uuid))
                .andExpect(status().isOk())
                .andExpect(view().name("TgUser"));
    }

    /**
     * test check that we can save ne user
     */
    @Test
    void saveNewTgUser() throws Exception {
        String name = "test";
        String description = "test user";
        TgUser theTgUser = new TgUser();
        theTgUser.setName(name);
        theTgUser.setInviterDescription(name);
        mockMvc.perform(post("/newTgUser").flashAttr("tgUser", theTgUser))
                .andExpect(status().isOk())
                .andExpect(view().name("Success"));
        Mockito.verify(userService,Mockito.times(1)).save(theTgUser);
    }
}
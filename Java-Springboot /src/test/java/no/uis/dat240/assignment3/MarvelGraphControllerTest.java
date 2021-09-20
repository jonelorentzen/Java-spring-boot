package no.uis.dat240.assignment3;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MarvelGraphControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Greetings from Spring Boot!")));

	}

	@WithMockUser("USER")
	@Test
	public void checkDegree() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/degree?id=TONY STARK").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.Node", equalTo("tony stark"))).andExpect(jsonPath("$.Degree", equalTo(1521)));

		mvc.perform(MockMvcRequestBuilders.get("/degree?id=vinay").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	@WithMockUser("USER")
	@Test
	public void checkNeighbors() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/neighbors?id=Iron Man").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.Node", equalTo("iron man"))).andExpect(jsonPath("$.Neighbors").isArray())
				.andExpect(jsonPath("$.Neighbors", hasSize(1521)))
				.andExpect(jsonPath("$.Neighbors", hasItem("magneto/magnus/eric")));
//		

		mvc.perform(MockMvcRequestBuilders.get("/neighbors?id=Magneto").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.Node", equalTo("magneto"))).andExpect(jsonPath("$.Neighbors").isArray())
				.andExpect(jsonPath("$.Neighbors", hasSize(526)))
				.andExpect(jsonPath("$.Neighbors", hasItem("clinton bill")));

		mvc.perform(MockMvcRequestBuilders.get("/degree?id=espen askeladen").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@WithMockUser("USER")
	@Test
	public void checkEdge() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/checkedge?id1=iron man&id2=jarvis edwin").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.Node1", equalTo("iron man")))
				.andExpect(jsonPath("$.Node2", equalTo("jarvis edwin"))).andExpect(jsonPath("$.EdgeExists", equalTo(true)));

		mvc.perform(MockMvcRequestBuilders.get("/checkedge?id1=zzzax&id2=zota").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.Node1", equalTo("zzzax")))
				.andExpect(jsonPath("$.Node2", equalTo("zota"))).andExpect(jsonPath("$.EdgeExists", equalTo(false)));

		mvc.perform(MockMvcRequestBuilders.get("/checkedge?id1=tony stark&id2=vinay setty")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	@Test
	public void find_nologin_401() throws Exception {
		mvc.perform(get("/shortestpath/")).andDo(print()).andExpect(status().isUnauthorized());
	}

}

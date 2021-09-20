package no.uis.dat240.assignment3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.BufferedReader;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MarvelGraphController {

    HashMap<String, HashSet<String>> heroMap = new HashMap<>();

    public MarvelGraphController() throws FileNotFoundException, IOException {
        try (BufferedReader marvelHero = new BufferedReader(new FileReader("marvel-hero-network.csv"))) {
            for (String line; (line = marvelHero.readLine()) != null; ) {
                String[] splitted = line.toLowerCase().trim().split(",");

                String delEn = splitted[0];
                String[] delEnSplit = delEn.split("/");

                String delTo = splitted[1];
                String[] delToSplit = delTo.split("/");

                for (String s : delEnSplit) {
                    String str = s.trim();
                    if (!heroMap.containsKey(str)) {
                        HashSet<String> liste = new HashSet<>();
                        heroMap.put(str, liste);
                    }
                    heroMap.get(str).add(delTo.trim());
                }

                for (String s : delToSplit) {
                    String str = s.trim();
                    if (!heroMap.containsKey(str)) {
                        HashSet<String> liste = new HashSet<>();
                        heroMap.put(str, liste);
                    }
                    heroMap.get(str).add(delEn.trim());
                }
            }

        }
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping(path = "/degree", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map getNodeDegree(@RequestParam String id) {
        if (heroMap.get(id.toLowerCase()) == null) throw new NodeNotFoundException();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Node", id.toLowerCase());
        map.put("Degree", heroMap.get(id.toLowerCase()).size());
        return map;
    }

    @GetMapping(path = "/neighbors", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map getNodeNeighbors(@RequestParam String id) {
        HashMap<String, Object> map = new HashMap<>();
        if (heroMap.get(id.toLowerCase()) == null) throw new NodeNotFoundException();
        map.put("Node", id.toLowerCase());
        map.put("Neighbors", heroMap.get(id.toLowerCase()));
        return map;
    }

    @GetMapping(path = "/checkedge", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map getNodeCheckEdge(@RequestParam String id1, @RequestParam String id2) {
        if (heroMap.get(id1.toLowerCase()) == null) throw new NodeNotFoundException();
        if (heroMap.get(id2.toLowerCase()) == null) throw new NodeNotFoundException();
        HashMap<String, Object> map = new HashMap<>();
        id1 = id1.trim().toLowerCase();
        id2 = id2.trim().toLowerCase();
        map.put("Node1", id1);
        map.put("Node2", id2);
        map.put("EdgeExists", false);
        if (heroMap.containsKey(id1)) {
            map.put("EdgeExists", heroMap.get(id1).contains(id2));
        }
        return map;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Given node id not found!")
    public class NodeNotFoundException extends RuntimeException {

    }
}
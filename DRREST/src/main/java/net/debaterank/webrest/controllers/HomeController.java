package net.debaterank.webrest.controllers;

import net.debaterank.webrest.models.home.DebaterRanking;
import net.debaterank.webrest.models.home.SchoolSearchResult;
import net.debaterank.webrest.models.home.TableSearchResult;
import net.debaterank.webrest.services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/home/*")
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    @Autowired
    private HomeService homeService;

    private static final int MIN_YEAR = 2000;
    private static final int PAGE_SIZE = 25;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    class InvalidEventException extends Exception {
        InvalidEventException(String event) {
            super(event + " is not a valid event");
        }
    }

    class InvalidYearException extends Exception {
        InvalidYearException(int year) {
            super(year + " is not a valid year");
        }
    }

    @GetMapping("/schoolSearch")
    public SchoolSearchResult[] getSchoolSearch(@RequestParam(name="query") String query) {
        return homeService.schoolSearch(query);
    }

    @GetMapping("/debaterTableSearch")
    public TableSearchResult[] getDebaterTableSearch(
            @RequestParam(name="query") String query,
            @RequestParam(name="season", required = false) Integer season,
            @RequestParam(name="school", required = false) Integer school) {
        Optional<Integer> year = Optional.ofNullable(season);
        Optional<Integer> schoolID = Optional.ofNullable(school);
        return homeService.debaterTableSearch(PAGE_SIZE, query, year, schoolID);
    }

    @GetMapping("/getSeasons")
    public Integer[] getSeasons() {
        return homeService.getSeasons();
    }

    @GetMapping("/pageLength")
    public long getPageLength(
            @RequestParam(name="event") String event,
            @RequestParam(name="season", required = false) Integer season) {
        Optional<Integer> year = Optional.ofNullable(season);
        if(!event.equals("CX") && !event.equals("PF") && !event.equals("LD"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid event");
        if(year.isPresent()) {
            if(year.get() < MIN_YEAR || year.get() > getMaxYear())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, year.get() + " is not a valid year");
        }

        return homeService.getPagesLength(PAGE_SIZE, event, year);
    }

    /**
     * Returns
     * @param page Page number
     * @return Array of ranking objects
     */
    @GetMapping("/rankingPage")
    public DebaterRanking[] getRankingPage(
            @RequestParam(name="page") int page,
            @RequestParam(name="event") String event,
            @RequestParam(name= "season", required = false) Integer season)
            throws Exception {
        Optional<Integer> year = Optional.ofNullable(season);

        if(!event.equals("CX") && !event.equals("PF") && !event.equals("LD"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid event");
        if(year.isPresent()) {
            if(year.get() < MIN_YEAR || year.get() > getMaxYear())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, year.get() + " is not a valid year");
        }

        return homeService.getRankings(page, PAGE_SIZE, event, year);
    }

    private int getMaxYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        if(cal.get(Calendar.MONTH) >= 9)
            return cal.get(Calendar.YEAR);
        else
            return cal.get(Calendar.YEAR) - 1;
    }


}

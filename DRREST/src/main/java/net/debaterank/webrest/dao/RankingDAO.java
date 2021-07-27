package net.debaterank.webrest.dao;

import net.debaterank.webrest.models.Debater;
import net.debaterank.webrest.models.Rating;
import net.debaterank.webrest.models.School;
import net.debaterank.webrest.models.Team;
import net.debaterank.webrest.models.home.DBDebaterTableSearchResult;
import net.debaterank.webrest.models.home.DebaterRanking;
import net.debaterank.webrest.models.home.SchoolSearchResult;
import net.debaterank.webrest.models.home.TableSearchResult;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Repository
@Transactional
public class RankingDAO {

    private static final int MAX_SEARCH_RESULTS = 5;

    @PersistenceContext
    private EntityManager entityManger;

    public SchoolSearchResult[] schoolSearch(String query) {
        List<SchoolSearchResult> results = entityManger.createNativeQuery(
                "select id, name as schoolName from School s where lower(name) like :query",
                SchoolSearchResult.class)
                .setParameter("query", "%" + query.toLowerCase() + "%")
                .setMaxResults(MAX_SEARCH_RESULTS)
                .getResultList();
        return results.toArray(new SchoolSearchResult[0]);
    }

    public DBDebaterTableSearchResult[] debaterTableSearch(String query, Optional<Integer> school, String event, Optional<Integer> season) {
        List<DBDebaterTableSearchResult> searchResults = entityManger.createNativeQuery(
                "select r.id, d.first, d.last, s.name as schoolName, rownum.ranking " +
                        "from Rating r " +
                        "left join (select id, ROW_NUMBER () OVER (order by rating desc) as ranking from Rating where " + (season.map(integer -> ("cast(season as int)=" + integer + " and ")).orElse("")) + "event=:event) as rownum on r.id=rownum.id " +
                        "inner join Debater as d on cast(r.uid as int)=d.id " +
                        "inner join School as s on d.school_id=s.id " +
                        "where " +
                        (season.map(integer -> ("cast(r.season as int)=" + integer + " and ")).orElse("")) +
                        "event=:event and " +
                        (school.map(s -> ("school=" + s + " and ")).orElse("")) +
                        "(lower(coalesce(d.first, '') || ' ' || coalesce(d.middle, '') || coalesce(d.last, '') || ' ' || coalesce(d.suffix, '')) like :query or " +
                        "lower(coalesce(d.first, '') || ' ' || coalesce(d.last, '')) like :query or " +
                        "lower(coalesce(d.last, '') || ' ' || coalesce(d.first, '')) like :query or " +
                        "lower(left(coalesce(d.first, ''), 1) || left(coalesce(d.last, ''), 1)) like :query)",
                DBDebaterTableSearchResult.class)
                .setParameter("query", "%" + query.toLowerCase() + "%")
                .setParameter("event", event)
                .setMaxResults(MAX_SEARCH_RESULTS)
                .getResultList();
        return searchResults.toArray(new DBDebaterTableSearchResult[0]);
    }

    public int getPagesLength(int pageSize, Optional<Integer> season, String event) {
        if(event.equals("LD"))
            return getLDPagesLength(pageSize, season);
        else if(event.equals("PF") || event.equals("CX"))
            return getDuoPagesLength(pageSize, season, event);
        else
            return 0;
    }

    public int getDuoPagesLength(int pageSize, Optional<Integer> season, String event) {
        List ratingResults = entityManger.createQuery(
                "select distinct r.numberOfResults, t.one, t.two, r.rating, r.ratingDeviation, r.season " +
                        "from Rating r " +
                        "inner join Team as t on cast(r.uid as int)=t.id " +
                        "inner join Debater as d on t.one=d.id or t.two=d.id " +
                        "inner join School as s on d.school=s.id " +
                        "where" +
                        (season.map(integer -> (" cast(r.season as int)=" + integer + " and")).orElse("")) +
                        " event=:event"+
                        " order by r.rating desc")
                .setParameter("event", event)
                .getResultList();

        return (int)Math.ceil(ratingResults.size() / pageSize);
    }

    public int getLDPagesLength(int pageSize, Optional<Integer> season) {
        List ratingResults = entityManger.createQuery(
                "select r.numberOfResults, d, s, r.rating, r.ratingDeviation, r.season " +
                        "from Rating r " +
                        "inner join Debater as d on cast(r.uid as int)=d.id " +
                        "inner join School as s on d.school=s.id " +
                        "where" +
                        (season.map(integer -> (" cast(r.season as int)=" + integer + " and")).orElse("")) +
                        " event='LD'"+
                        " order by r.rating desc")
                .getResultList();

        return (int)Math.ceil(ratingResults.size() / pageSize);
    }

    public DebaterRanking[] getRankingTable(int page, int pageSize, Optional<Integer> season, String event) {
        if(event.equals("LD"))
            return getLDRankingTable(page, pageSize, season);
        else if(event.equals("PF") || event.equals("CX"))
            return getDuoRankingTable(page, pageSize, season, event);
        else
            return new DebaterRanking[0];
    }

    public DebaterRanking[] getDuoRankingTable(int page, int pageSize, Optional<Integer> season, String event) {
        DebaterRanking[] arr = new DebaterRanking[pageSize];
        List ratingResults = entityManger.createQuery(
                "select distinct r.numberOfResults, t.one, t.two, r.rating, r.ratingDeviation, r.season " +
                        "from Rating r " +
                        "inner join Team as t on cast(r.uid as int)=t.id " +
                        "inner join Debater as d on t.one=d.id or t.two=d.id " +
                        "inner join School as s on d.school=s.id " +
                        "where" +
                        (season.map(integer -> (" cast(r.season as int)=" + integer + " and")).orElse("")) +
                        " event=:event"+
                        " order by r.rating desc")
                .setParameter("event", event)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        int ranking = page * pageSize + 1;
        int index = 0;

        for(Object[] result : (List<Object[]>)ratingResults) {
            DebaterRanking dr = new DebaterRanking();
            dr.ranking = ranking++;
            dr.numRounds = (int)result[0];
            Debater d1 = (Debater)result[1];
            Debater d2 = (Debater)result[2];
            if(d1 != null) {
                dr.debaterName = d1.toString();
                if(d1.getSchool() != null)
                    dr.schoolName = d1.getSchool().toString();
                else if(d2 != null && d2.getSchool() != null)
                    dr.schoolName = d2.getSchool().toString();
            }
            if(d2 != null) {
                if(dr.debaterName.isEmpty())
                    dr.debaterName = d2.toString();
                else
                    dr.debaterName += " | " + d2;
            }
            dr.rating = Math.floor(((double)result[3]) * 100) / 100; // two decimal pts
            dr.ratingDeviation = Math.floor(((double)result[4]) * 100) / 100; // two decimal pts
            dr.season = Integer.parseInt(String.valueOf(result[5]));
            arr[index++] = dr;
        }

        return arr;
    }

    public DebaterRanking[] getLDRankingTable(int page, int pageSize, Optional<Integer> season) {
        DebaterRanking[] arr = new DebaterRanking[pageSize];

        List ratingResults = entityManger.createQuery(
                "select r.numberOfResults, d, s, r.rating, r.ratingDeviation, r.season " +
                        "from Rating r " +
                        "inner join Debater as d on cast(r.uid as int)=d.id " +
                        "inner join School as s on d.school=s.id " +
                        "where" +
                        (season.map(integer -> (" cast(r.season as int)=" + integer + " and")).orElse("")) +
                        " event='LD'"+
                        " order by r.rating desc")
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        int ranking = page * pageSize + 1;
        int index = 0;

        for(Object[] result : (List<Object[]>)ratingResults) {
            DebaterRanking dr = new DebaterRanking();
            dr.key = ranking;
            dr.ranking = ranking;
            ranking++;
            dr.numRounds = (int) result[0];
            dr.debaterName = ((Debater) result[1]).toString();
            dr.schoolName = ((School) result[2]).toString();
            dr.rating = Math.floor(((double) result[3]) * 100) / 100; // two decimal pts
            dr.ratingDeviation = Math.floor(((double) result[4]) * 100) / 100; // two decimal pts
            dr.season = Integer.parseInt(String.valueOf(result[5]));
            arr[index++] = dr;
        }

        return arr;
    }
}

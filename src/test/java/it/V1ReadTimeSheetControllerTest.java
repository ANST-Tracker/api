package it;

import com.anst.sd.api.adapter.rest.task.log.dto.TimeSheetDto;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class V1ReadTimeSheetControllerTest extends AbstractIntegrationTest{
    private static final String API_URL = "/time-sheet";
    protected LocalDate now;
    protected String start;
    protected String end;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        epicTask = createEpic(user, project);
        timeEstimation = createTimeEstimation(TimeUnit.HOURS, 3);
        now = LocalDate.now();
        start = now.minusDays(1).toString();
        end = now.plusDays(1).toString();
    }

    @Test
    void getTimeSheetByProject_successfully() throws Exception {
        Log log = createLog(epicTask, user, "test content", timeEstimation, now);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/project/" + project.getId())
                .param("startDate", start)
                .param("endDate", end))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TimeSheetDto> result = getListFromResponse(response, TimeSheetDto.class);
        assertEquals(1, result.size());
        assertEquals(log.getId(), result.get(0).getId());
        assertEquals(log.getTimeEstimation().getTimeUnit(), result.get(0).getTimeEstimation().getTimeUnit());
        assertEquals(log.getTimeEstimation().getAmount(), result.get(0).getTimeEstimation().getAmount());
        assertEquals(log.getComment(), result.get(0).getComment());
        assertEquals(log.getDate(), result.get(0).getDate());
        assertEquals(log.getTask().getSimpleId(), result.get(0).getTaskSimpleId());
        assertEquals(log.getTask().getName(), result.get(0).getTaskName());
    }

    @Test
    void getTimeSheet_successfully() throws Exception {
        Log log = createLog(epicTask, user, "test content", timeEstimation, now);

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/all-projects")
                .param("startDate", start)
                .param("endDate", end))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TimeSheetDto> result= getListFromResponse(response, TimeSheetDto.class);
        assertEquals(1, result.size());
        assertEquals(log.getId(), result.get(0).getId());
        assertEquals(log.getTimeEstimation().getTimeUnit(), result.get(0).getTimeEstimation().getTimeUnit());
        assertEquals(log.getTimeEstimation().getAmount(), result.get(0).getTimeEstimation().getAmount());
        assertEquals(log.getComment(), result.get(0).getComment());
        assertEquals(log.getDate(), result.get(0).getDate());
        assertEquals(log.getTask().getSimpleId(), result.get(0).getTaskSimpleId());
        assertEquals(log.getTask().getName(), result.get(0).getTaskName());
    }

    @Test
    void getTimeSheetByProject_emptyList_successfully() throws Exception {
        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/project/" + project.getId())
                .param("startDate", start)
                .param("endDate", end))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TimeSheetDto> result = getListFromResponse(response, TimeSheetDto.class);
        assertEquals(0, result.size());
    }

    @Test
    void getTimeSheet_emptyList_successfully() throws Exception {
        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/all-projects")
                .param("startDate", start)
                .param("endDate", end))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TimeSheetDto> result = getListFromResponse(response, TimeSheetDto.class);
        assertEquals(0, result.size());
    }

    @Test
    void getTimeSheetByProject_notAccess() throws Exception {
        createLog(epicTask, user, "test content", timeEstimation, now);
        User user1 = createUser("Test", "Test", "Test", null, null);
        Project project1 = createTestProject(user1);

        performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL + "/project/" + project1.getId())
                .param("startDate", start)
                .param("endDate", end))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }
}

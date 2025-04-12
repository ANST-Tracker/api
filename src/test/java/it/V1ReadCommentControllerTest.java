package it;

import com.anst.sd.api.adapter.rest.task.comments.dto.CommentInfoDto;
import com.anst.sd.api.domain.task.AbstractTask;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1ReadCommentControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project/%s/task/%s/comments";

    @Test
    void getComments_successfully() throws Exception {
        user = createTestUser();
        reviewer = createTestReviewer();
        assignee = createTestAssignee();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        createComment(subtask, reviewer, "reviewerComment");
        createComment(subtask, assignee, "assigneeComment");
        createComment(subtask, user, "userComment");

        MvcResult response = performAuthenticated(user, MockMvcRequestBuilders
                .get(API_URL.formatted(project.getId(), subtask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<CommentInfoDto> commentInfoDtoList = getListFromResponse(response, CommentInfoDto.class);
        commentInfoDtoList.forEach(commentInfoDto -> {
            assertNotNull(commentInfoDto.getCreated());
            assertNotNull(commentInfoDto.getId());
            assertNotNull(commentInfoDto.getAuthorId());
            commentInfoDto.setCreated(null);
            commentInfoDto.setId(null);
            commentInfoDto.setAuthorId(null);
        });
        commentInfoDtoList.sort(Comparator.comparing(CommentInfoDto::getContent));
        assertEqualsToFile("/V1ReadCommentControllerTest/expectedComments.json", commentInfoDtoList);
    }
}

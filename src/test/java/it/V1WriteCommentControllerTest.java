package it;

import com.anst.sd.api.adapter.rest.task.comments.dto.CreateUpdateCommentDto;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class V1WriteCommentControllerTest extends AbstractIntegrationTest {
    private static final String API_URL = "/project/%s/task/%s/comments";

    @Test
    void createComment_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        CreateUpdateCommentDto createCommentDto = new CreateUpdateCommentDto()
                .setContent("test content");

        performAuthenticated(user, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subtask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(1, commentJpaRepository.count());
        Comment result = commentJpaRepository.findAll().get(0);
        assertEquals(createCommentDto.getContent(), result.getContent());
        assertEquals(user.getId(), result.getAuthor().getId());
        assertNotNull(result.getCreated());
        assertEquals(subtask.getId(), result.getTask().getId());
    }

    @Test
    void createComment_failed_noUserInProject() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        CreateUpdateCommentDto createCommentDto = new CreateUpdateCommentDto()
                .setContent("test content");

        performAuthenticated(reviewer, MockMvcRequestBuilders
                .post(API_URL.formatted(project.getId(), subtask.getSimpleId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updateComment_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        Comment comment = createComment(subtask, user, "test content");
        CreateUpdateCommentDto updateCommentDto = new CreateUpdateCommentDto()
                .setContent("new content");

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subtask.getSimpleId()) + "/" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(1, commentJpaRepository.count());
        Comment result = commentJpaRepository.findAll().get(0);
        assertEquals(updateCommentDto.getContent(), result.getContent());
        assertEquals(user.getId(), result.getAuthor().getId());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
        assertEquals(subtask.getId(), result.getTask().getId());
    }

    @Test
    void updateComment_failed_authorIsAnotherUser() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        reviewer = createTestReviewer();
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        Comment comment = createComment(subtask, reviewer, "test content");
        CreateUpdateCommentDto createCommentDto = new CreateUpdateCommentDto()
                .setContent("test content");

        performAuthenticated(user, MockMvcRequestBuilders
                .put(API_URL.formatted(project.getId(), subtask.getSimpleId()) + "/" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andDo(print())

                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteComment_successfully() throws Exception {
        user = createTestUser();
        project = createTestProject(user);
        AbstractTask subtask = createSubtask(user, project, reviewer, assignee, null);
        Comment comment = createComment(subtask, user, "test content");

        performAuthenticated(user, MockMvcRequestBuilders
                .delete(API_URL.formatted(project.getId(), subtask.getSimpleId()) + "/" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(0, commentJpaRepository.count());
    }
}

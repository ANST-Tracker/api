package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTagUseCaseTest extends AbstractUnitTest {
    private CreateTagUseCase useCase;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        useCase = new CreateTagUseCase(userRepository, tagRepository);
    }

    @Test
    void create_successfully() {
        Long userId = 1L;
        Tag tag = new Tag();
        tag.setName("TestTag");
        User user = new User();
        user.setId(userId);

        when(tagRepository.existsByNameAndUserId(tag.getName(), userId)).thenReturn(false);
        when(userRepository.getById(userId)).thenReturn(user);
        when(tagRepository.save(tag)).thenReturn(tag);
        Tag result = useCase.create(tag, userId);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(tagRepository).save(tag);
    }

    @Test
    void create_TagAlreadyExists() {
        Long userId = 1L;
        Tag tag = new Tag();
        tag.setName("ExistingTag");

        when(tagRepository.existsByNameAndUserId(tag.getName(), userId)).thenReturn(true);

        assertThrows(TagValidationException.class, () -> useCase.create(tag, userId));
        verify(tagRepository, never()).save(any());
    }
}

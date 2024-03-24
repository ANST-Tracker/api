package com.anst.sd.api;

import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.fw.spring.JacksonConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUnitTest {
    protected ObjectMapper objectMapper;

    public AbstractUnitTest() {
        JacksonConfig jacksonConfig = new JacksonConfig();
        objectMapper = jacksonConfig.objectMapper();
        objectMapper.setDefaultPrettyPrinter(new MyDefaultPrettyPrinter());
    }

    protected <T> T readFromFile(String fileName, Class<T> type) {
        String content = readFile(fileName);
        try {
            return objectMapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void assertEqualsToFile(String fileName, Object value) {
        String expected = readFile(fileName);
        try {
            String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
            Assertions.assertEquals(expected, actual);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected String readFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            return Files.readString(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected static class MyDefaultPrettyPrinter extends DefaultPrettyPrinter {
        private static final long serialVersionUID = 9003705494148916471L;

        public MyDefaultPrettyPrinter() {
            this._objectIndenter = new DefaultIndenter("  ", System.lineSeparator());
            this._arrayIndenter = _objectIndenter;
        }

        @Override
        public DefaultPrettyPrinter createInstance() {
            return new MyDefaultPrettyPrinter();
        }

        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
            jg.writeRaw(": ");
        }
    }

    protected User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setTelegramId("telegramId");
        user.setPassword("password");
        user.setUsername("username");
        return user;
    }

    protected Project createTestProject(User user) {
        Project project = new Project();
        project.setId(1L);
        project.setUser(user);
        project.setName("test");
        project.setProjectType(ProjectType.BASE);
        return project;
    }
}

package com.jaydin.taskapi;

import com.jaydin.taskapi.model.Task;
import com.jaydin.taskapi.repository.JdbcTaskRepository;
import com.jaydin.taskapi.service.DatabaseConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTaskRepositoryTest {

    private JdbcTaskRepository repo;

    @BeforeEach
    void setUp() {
        DatabaseConnectionProvider connectionProvider = new DatabaseConnectionProvider(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "sa",
                "pa"
        );
        repo = new JdbcTaskRepository(connectionProvider);
        repo.init();
    }

    @Test
    void fullCrudLifecycle() {
        // save
        Task newTask = new Task(0, "Write tests", "Cover repository layer", false, null);
        Task saved = repo.save(newTask);
        assertNotNull(saved.getId());
        int id = saved.getId();

        // findById
        Task fetched = repo.findById(id);
        assertNotNull(fetched);
        assertEquals("Write tests", fetched.getTitle());
        assertFalse(fetched.isCompleted());

        // modify + update
        fetched.setTitle("Write tests (updated)");
        fetched.setCompleted(true);
        Task updated = repo.update(fetched);
        assertEquals("Write tests (updated)", updated.getTitle());

        // findById again — confirm update persisted
        Task refetched = repo.findById(id);
        assertEquals("Write tests (updated)", refetched.getTitle());
        assertTrue(refetched.isCompleted());

        // findAll — confirm it's in the full list
        List<Task> all = repo.findAll();
        assertTrue(all.stream().anyMatch(t -> t.getId() == id));

        // deleteById
        repo.deleteById(id);

        // findById — confirm gone
        Task afterDelete = repo.findById(id);
        assertNull(afterDelete);
    }
}

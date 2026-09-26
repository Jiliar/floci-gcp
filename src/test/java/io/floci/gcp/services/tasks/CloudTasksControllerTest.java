package io.floci.gcp.services.tasks;

import com.google.cloud.tasks.v2.CreateQueueRequest;
import com.google.cloud.tasks.v2.ListQueuesRequest;
import com.google.cloud.tasks.v2.ListQueuesResponse;
import com.google.cloud.tasks.v2.Queue;
import io.floci.gcp.core.storage.InMemoryStorage;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudTasksControllerTest {

    private static final String PARENT = "projects/p1/locations/us-east1";

    private CloudTasksController controller;

    @BeforeEach
    void setUp() {
        controller = new CloudTasksController(new CloudTasksService(new InMemoryStorage<>(), new InMemoryStorage<>()));
    }

    @Test
    void wellFormedParentCreatesAndListsQueue() {
        RecordingObserver<Queue> created = new RecordingObserver<>();
        controller.createQueue(CreateQueueRequest.newBuilder()
                .setParent(PARENT)
                .setQueue(Queue.newBuilder().setName(PARENT + "/queues/q1"))
                .build(), created);
        assertNull(created.error);
        assertEquals(PARENT + "/queues/q1", created.values.get(0).getName());

        RecordingObserver<ListQueuesResponse> listed = new RecordingObserver<>();
        controller.listQueues(ListQueuesRequest.newBuilder().setParent(PARENT).build(), listed);
        assertNull(listed.error);
        assertEquals(1, listed.values.get(0).getQueuesCount());
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "", "projects/p", "projects/p/regions/r"})
    void malformedParentIsInvalidArgument(String parent) {
        RecordingObserver<Queue> created = new RecordingObserver<>();
        controller.createQueue(CreateQueueRequest.newBuilder()
                .setParent(parent)
                .setQueue(Queue.newBuilder().setName("q1"))
                .build(), created);
        assertInvalidArgument(created);

        RecordingObserver<ListQueuesResponse> listed = new RecordingObserver<>();
        controller.listQueues(ListQueuesRequest.newBuilder().setParent(parent).build(), listed);
        assertInvalidArgument(listed);
    }

    private static void assertInvalidArgument(RecordingObserver<?> observer) {
        assertTrue(observer.values.isEmpty());
        StatusRuntimeException sre = assertInstanceOf(StatusRuntimeException.class, observer.error);
        assertEquals(Status.Code.INVALID_ARGUMENT, sre.getStatus().getCode());
    }

    private static final class RecordingObserver<T> implements StreamObserver<T> {
        final List<T> values = new ArrayList<>();
        Throwable error;

        @Override
        public void onNext(T value) {
            values.add(value);
        }

        @Override
        public void onError(Throwable t) {
            error = t;
        }

        @Override
        public void onCompleted() {
        }
    }
}

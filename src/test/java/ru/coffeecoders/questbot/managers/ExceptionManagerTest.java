package ru.coffeecoders.questbot.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.logs.LogSender;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class ExceptionManagerTest {

    @Mock
    private LogSender logger;
    @Mock
    private Exception e;

    @InjectMocks
    private ExceptionManager exceptionManager;

    @Test
    public void shouldWriteLogWhenCalled() {
        when(e.getMessage()).thenReturn("message");
        doNothing().when(logger).error(anyString());

        exceptionManager.logException(e);
        verify(logger).error(anyString());
    }
}
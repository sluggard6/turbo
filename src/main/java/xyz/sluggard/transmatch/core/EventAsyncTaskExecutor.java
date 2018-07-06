package xyz.sluggard.transmatch.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.core.task.AsyncTaskExecutor;

public class EventAsyncTaskExecutor implements AsyncTaskExecutor {

	@Override
	public void execute(Runnable task) {

	}

	@Override
	public void execute(Runnable task, long startTimeout) {

	}

	@Override
	public Future<?> submit(Runnable task) {
		return null;
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return null;
	}

}

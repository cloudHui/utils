package threadtutil.timer;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface Runners<T extends Runnable> {
	CompletableFuture<T> run(T var1);
}
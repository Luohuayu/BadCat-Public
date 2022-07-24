package moe.badcat.hook;

import moe.badcat.module.modules.ModuleSkinSave;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SkinCompletionHook<V> implements CompletionService<V> {
    private final ModuleSkinSave moduleAWSkinSave;
    private final CompletionService<V> oldCompletionService;

    public SkinCompletionHook(ModuleSkinSave moduleAWSkinSave, CompletionService<V> oldCompletionService) {
        this.moduleAWSkinSave = moduleAWSkinSave;
        this.oldCompletionService = oldCompletionService;
    }

    @Override
    public Future<V> submit(Callable<V> task) {
        moduleAWSkinSave.hookSubmit(task);
        return this.oldCompletionService.submit(task);
    }

    @Override
    public Future<V> submit(Runnable task, V result) {
        moduleAWSkinSave.hookSubmit(task);
        return this.oldCompletionService.submit(task, result);
    }

    @Override
    public Future<V> take() throws InterruptedException {
        return this.oldCompletionService.take();
    }

    @Override
    public Future<V> poll() {
        return this.oldCompletionService.poll();
    }

    @Override
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return this.oldCompletionService.poll(timeout, unit);
    }
}

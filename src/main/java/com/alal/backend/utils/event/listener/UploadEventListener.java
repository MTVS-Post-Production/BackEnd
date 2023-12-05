package com.alal.backend.utils.event.listener;

import com.alal.backend.utils.event.UploadRollBackEvent;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UploadEventListener {
    private final Storage storage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleUploadEvent(UploadRollBackEvent uploadRollBackEvent) {
        uploadRollBackEvent.deleteUrl(storage);
    }
}

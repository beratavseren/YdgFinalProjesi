package org.example.ydgbackend.Entity;

public enum TransactionStuation {
    READY,
    PARTIALLY_FILLED,
    FILLED,
    //WORKER CANCEL İSTEYECEK ADMİN ONAYLAYACAK YA DA ADMİN DİREKT KENDİSİ CANCEL KARARI VERECEK
    WAITING_CANCELLATION,
    CANCELED
}

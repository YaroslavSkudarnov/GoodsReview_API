package ru.goodsReview.core.db.controller;

/*
 *  Date: 13.11.11
 *  Time: 10:59
 *  Author:
 *     Vanslov Evgeny
 *     vans239@gmail.com
 */

import ru.goodsReview.core.db.exception.StorageException;
import ru.goodsReview.core.model.Source;

public interface SourceController {
    public long addSource(Source source) throws StorageException;
    public Source getSourceById(long source_id);
}

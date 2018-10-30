package com.dcs4u.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Add this interface to any class in order to get direct access to SLF4j features.
 *
 * The method getLogger(...) creates a new logger instance only the first time it's called for each class,
 * then it always gets back the same logger that has been stored in a ConcurrentHashMap.
 * So here is a smooth, fast and thread safe SLF4j integration :)
 */
interface Loggable {
    val log: Logger get() = LoggerFactory.getLogger(this.javaClass.name)
}
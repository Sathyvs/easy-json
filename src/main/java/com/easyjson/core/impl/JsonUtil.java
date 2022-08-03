/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package com.easyjson.core.impl;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import com.easyjson.core.JsonArray;
import com.easyjson.core.JsonObject;
import java.time.Instant;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Implementation utilities (details) affecting the way JSON objects are wrapped.
 */
public final class JsonUtil {

  public static final Base64.Encoder BASE64_ENCODER;
  public static final Base64.Decoder BASE64_DECODER;

  static {
    BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    BASE64_DECODER = Base64.getUrlDecoder();
  }

  /**
   * Wraps well known java types to adhere to the Json expected types.
   * <ul>
   *   <li>{@code Map} will be wrapped to {@code JsonObject}</li>
   *   <li>{@code List} will be wrapped to {@code JsonArray}</li>
   *   <li>{@code Instant} will be converted to iso date {@code String}</li>
   *   <li>{@code byte[]} will be converted to base64 {@code String}</li>
   *   <li>{@code Enum} will be converted to enum name {@code String}</li>
   * </ul>
   *
   * @param val java type
   * @return wrapped type or {@code val} if not applicable.
   */
  public static Object wrapJsonValue(Object val) {
    if (val == null) {
      return null;
    }

    // perform wrapping
    if (val instanceof Map) {
      val = new JsonObject((Map) val);
    } else if (val instanceof List) {
      val = new JsonArray((List) val);
    } else if (val instanceof Instant) {
      val = ISO_INSTANT.format((Instant) val);
    } else if (val instanceof byte[]) {
      val = BASE64_ENCODER.encodeToString((byte[]) val);
    } else if (val instanceof Enum) {
      val = ((Enum) val).name();
    }

    return val;
  }

  public static final Function<Object, ?> DEFAULT_CLONER = o -> {
    throw new IllegalStateException("Illegal type in Json: " + o.getClass());
  };

  @SuppressWarnings("unchecked")
  public static Object deepCopy(Object val, Function<Object, ?> copier) {
    if (val == null) {
      // OK
    } else if (val instanceof Number) {
      // OK
    } else if (val instanceof Boolean) {
      // OK
    } else if (val instanceof String) {
      // OK
    } else if (val instanceof Character) {
      // OK
    } else if (val instanceof CharSequence) {
      // CharSequences are not immutable, so we force toString() to become immutable
      val = val.toString();
    } else if (val instanceof Map) {
      val = (new JsonObject((Map) val)).copy(copier);
    } else if (val instanceof List) {
      val = (new JsonArray((List) val)).copy(copier);
    } else if (val instanceof byte[]) {
      // OK
    } else if (val instanceof Instant) {
      // OK
    } else if (val instanceof Enum) {
      // OK
    } else {
      val = copier.apply(val);
    }
    return val;
  }

  public static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
    Iterable<T> iterable = () -> sourceIterator;
    return StreamSupport.stream(iterable.spliterator(), false);
  }
}

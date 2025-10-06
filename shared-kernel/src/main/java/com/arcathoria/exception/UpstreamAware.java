package com.arcathoria.exception;

import java.util.Optional;

public interface UpstreamAware {

    Optional<UpstreamInfo> getUpstreamInfo();
}

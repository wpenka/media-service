package balade.media.service;

import balade.media.service.domain.Slice;

import java.io.IOException;

public class InconsistantSlideException extends IOException {
    public InconsistantSlideException(Slice slice) {
        super(slice.toString());
    }
}

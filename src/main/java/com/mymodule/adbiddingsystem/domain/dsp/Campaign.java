package com.mymodule.adbiddingsystem.domain.dsp;

import com.mymodule.adbiddingsystem.common.messages.Dimension;

import java.util.List;

public record Campaign(String id, String targetedCountry, String targetedDomain, List<Dimension>dimensions) {
}

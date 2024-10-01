package com.anst.sd.api.app.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class DateRangeFilter {
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
}

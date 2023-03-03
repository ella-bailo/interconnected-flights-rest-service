package com.github.ellabailo.interconnectedflightsrestservice.client;

import com.github.ellabailo.interconnectedflightsrestservice.model.Schedule;
import reactor.core.publisher.Mono;

public interface SchedulesClient {
  Mono<Schedule> getSchedule();
}

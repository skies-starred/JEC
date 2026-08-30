package foo.starred.jec.api.scheduling

import foo.starred.jec.annotations.Load
import foo.starred.snowbird.api.scheduling.scheduler.impl.AbstractScheduler

@Load
object Scheduler : AbstractScheduler()

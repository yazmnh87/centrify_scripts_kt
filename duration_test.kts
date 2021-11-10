@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
@file:A
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import java.util.Date
import kotlinx.coroutines.*
//import kotlin.coroutines

//kotlinc -script duration_test.kts @args.kts=Xopt-in=kotlin.RequiresOptIn  <--example of compiler options
val dt = Date()
println(dt.time)

runBlocking{
    delay(1000)
}

//@ExperimentalTime
//val duration = Duration
//@OptIn(ExperimentalTime::class)
//println(duration.minutes(dt.time))

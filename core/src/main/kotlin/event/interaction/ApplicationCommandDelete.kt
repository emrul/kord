package dev.kord.core.event.interaction

import dev.kord.core.Kord
import dev.kord.core.entity.GuildApplicationCommand
import dev.kord.core.event.Event


class ApplicationCommandDeleteEvent(
    val command: GuildApplicationCommand,
    override val kord: Kord,
    override val shard: Int
) : Event
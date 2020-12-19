package dev.kord.core.behavior

import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.ApplicationCommandData
import dev.kord.core.entity.Entity
import dev.kord.core.entity.GlobalApplicationCommand
import dev.kord.core.entity.GuildApplicationCommand
import dev.kord.rest.builder.interaction.GlobalApplicationCommandModifyBuilder
import dev.kord.rest.builder.interaction.GuildApplicationCommandModifyBuilder
import dev.kord.rest.service.InteractionService

interface GlobalApplicationCommandBehavior : Entity {
    val applicationId: Snowflake
    val service: InteractionService
    suspend fun edit(
        name: String,
        description: String,
        builder: GlobalApplicationCommandModifyBuilder.() -> Unit
    ): GlobalApplicationCommand {
        val request = GlobalApplicationCommandModifyBuilder(name, description).apply(builder).toRequest()
        val response = service.modifyGlobalApplicationCommand(applicationId, id, request)
        val data = ApplicationCommandData.from(response)
        return GlobalApplicationCommand(data, service)
    }

    suspend fun delete() {
        service.deleteGlobalApplicationCommand(applicationId, id)
    }
}


interface GuildApplicationCommandBehavior : Entity {
    val applicationId: Snowflake
    val guildId: Snowflake
    val service: InteractionService
    suspend fun edit(
        name: String,
        description: String,
        builder: GuildApplicationCommandModifyBuilder.() -> Unit
    ): GuildApplicationCommand {
        val request = GuildApplicationCommandModifyBuilder(name, description).apply(builder).toRequest()
        val response = service.modifyGuildApplicationCommand(applicationId, guildId, id, request)
        val data = ApplicationCommandData.from(response)
        return GuildApplicationCommand(data, guildId, service)
    }

    suspend fun delete() {
        service.deleteGuildApplicationCommand(applicationId,guildId, id)
    }
}
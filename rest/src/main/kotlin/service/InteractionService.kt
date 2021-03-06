package dev.kord.rest.service

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordApplicationCommand
import dev.kord.common.entity.Snowflake
import dev.kord.rest.json.request.*
import dev.kord.rest.request.RequestHandler
import dev.kord.rest.route.Route
@KordPreview
class InteractionService(requestHandler: RequestHandler) : RestService(requestHandler) {
    suspend fun getGlobalApplicationCommands(applicationId: Snowflake): List<DiscordApplicationCommand> =
        call(Route.GlobalApplicationCommandsGet) {
            keys[Route.ApplicationId] = applicationId
        }

    suspend fun createGlobalApplicationCommand(
        applicationId: Snowflake,
        request: ApplicationCommandCreateRequest
    ): DiscordApplicationCommand = call(Route.GlobalApplicationCommandCreate) {
        keys[Route.ApplicationId] = applicationId
        body(ApplicationCommandCreateRequest.serializer(), request)
    }

    suspend fun modifyGlobalApplicationCommand(
        applicationId: Snowflake,
        commandId: Snowflake,
        request: ApplicationCommandModifyRequest
    ) = call(Route.GlobalApplicationCommandModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.CommandId] = commandId
        body(ApplicationCommandModifyRequest.serializer(), request)
    }

    suspend fun deleteGlobalApplicationCommand(applicationId: Snowflake, commandId: Snowflake) =
        call(Route.GlobalApplicationCommandDelete) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.CommandId] = commandId
        }

    suspend fun getGuildApplicationCommands(applicationId: Snowflake, guildId: Snowflake) =
        call(Route.GuildApplicationCommandsGet) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.GuildId] = guildId
        }

    suspend fun createGuildApplicationCommand(
        applicationId: Snowflake,
        guildId: Snowflake,
        request: ApplicationCommandCreateRequest
    ) =
        call(Route.GuildApplicationCommandCreate) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.GuildId] = guildId
            body(ApplicationCommandCreateRequest.serializer(), request)
        }

    suspend fun modifyGuildApplicationCommand(
        applicationId: Snowflake,
        guildId: Snowflake,
        commandId: Snowflake,
        request: ApplicationCommandModifyRequest
    ) = call(Route.GuildApplicationCommandModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.GuildId] = guildId
        keys[Route.CommandId] = commandId
        body(ApplicationCommandModifyRequest.serializer(), request)
    }

    suspend fun deleteGuildApplicationCommand(applicationId: Snowflake, guildId: Snowflake, commandId: Snowflake) =
        call(Route.GuildApplicationCommandDelete) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.GuildId] = guildId
            keys[Route.CommandId] = commandId
        }

    suspend fun createInteractionResponse(
        interactionId: Snowflake,
        interactionToken: String,
        request: InteractionResponseCreateRequest
    ) =
        call(Route.InteractionResponseCreate) {
            keys[Route.InteractionId] = interactionId
            keys[Route.InteractionToken] = interactionToken
            body(InteractionResponseCreateRequest.serializer(), request)
        }

    suspend fun modifyInteractionResponse(
        applicationId: Snowflake,
        interactionToken: String,
        request: InteractionResponseModifyRequest
    ) = call(Route.OriginalInteractionResponseModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.InteractionToken] = interactionToken
        body(InteractionResponseModifyRequest.serializer(), request)

    }

    suspend fun deleteOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String) =
        call(Route.OriginalInteractionResponseDelete) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.InteractionToken] = interactionToken
        }

    suspend fun createFollowupMessage(
        applicationId: Snowflake,
        interactionToken: String,
        multipart: MultipartFollowupMessageCreateRequest,
        wait: Boolean = false
    ) = call(Route.FollowupMessageCreate) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.InteractionToken] = interactionToken
        parameter("wait", "$wait")
        body(FollowupMessageCreateRequest.serializer(), multipart.request)
        multipart.file?.let { file(it) }

    }

    suspend fun deleteFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake) =
        call(Route.FollowupMessageDelete) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.InteractionToken] = interactionToken
            keys[Route.MessageId] = messageId
        }

    suspend fun modifyFollowupMessage(
        applicationId: Snowflake,
        interactionToken: String,
        messageId: Snowflake,
        request: FollowupMessageModifyRequest
    ) = call(Route.FollowupMessageModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.InteractionToken] = interactionToken
        keys[Route.MessageId] = messageId
        body(FollowupMessageModifyRequest.serializer(), request)
    }
}
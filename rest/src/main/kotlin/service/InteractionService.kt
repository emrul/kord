package dev.kord.rest.service

import dev.kord.common.entity.*
import dev.kord.rest.json.request.*
import dev.kord.rest.request.RequestHandler
import dev.kord.rest.route.Route

class InteractionService(requestHandler: RequestHandler) : RestService(requestHandler) {
    suspend fun getGlobalApplicationCommands(applicationId: Snowflake): List<DiscordApplicationCommand> =
        call(Route.GlobalApplicationCommandsGet) {
            keys[Route.ApplicationId] = applicationId
        }

    suspend fun createGlobalApplicationCommand(
        applicationId: Snowflake,
        request: GlobalApplicationCommandCreateRequest
    ): DiscordApplicationCommand = call(Route.GlobalApplicationCommandCreate) {
        keys[Route.ApplicationId] = applicationId
        body(GlobalApplicationCommandCreateRequest.serializer(), request)
    }

    suspend fun modifyGlobalApplicationCommand(
        applicationId: Snowflake,
        commandId: Snowflake,
        request: GlobalApplicationCommandModifyRequest
    ) = call(Route.GlobalApplicationCommandModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.CommandId] = commandId
        body(GlobalApplicationCommandModifyRequest.serializer(), request)
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
        request: GuildApplicationCommandCreateRequest
    ) =
        call(Route.GuildApplicationCommandCreate) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.GuildId] = guildId
            body(GuildApplicationCommandCreateRequest.serializer(), request)
        }

    suspend fun modifyGuildApplicationCommand(
        applicationId: Snowflake,
        guildId: Snowflake,
        commandId: Snowflake,
        request: GuildApplicationCommandModifyRequest
    ) = call(Route.GuildApplicationCommandModify) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.GuildId] = guildId
        keys[Route.CommandId] = commandId
        body(GuildApplicationCommandModifyRequest.serializer(), request)
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
        request: InteractionResponse
    ) =
        call(Route.InteractionResponseCreate) {
            keys[Route.InteractionId] = interactionId
            keys[Route.InteractionToken] = interactionToken
            body(InteractionResponse.serializer(), request)
        }

    suspend fun modifyInteractionResponse(
        interactionId: Snowflake,
        interactionToken: String,
        request: InteractionResponse
    ) = call(Route.OriginalInteractionResponseModify) {
        keys[Route.InteractionId] = interactionId
        keys[Route.InteractionToken] = interactionToken
        body(InteractionResponse.serializer(), request)

    }

    suspend fun deleteOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String) =
        call(Route.OriginalInteractionResponseDelete) {
            keys[Route.ApplicationId] = applicationId
            keys[Route.InteractionToken] = interactionToken
        }

    suspend fun createFollowupMessage(
        applicationId: Snowflake,
        interactionToken: String,
        request: MessageCreateRequest
    ) = call(Route.FollowupMessageCreate) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.InteractionToken] = interactionToken
        body(MessageCreateRequest.serializer(), request)
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
        request: MessageEditPatchRequest
    ) = call(Route.EditMessagePatch) {
        keys[Route.ApplicationId] = applicationId
        keys[Route.InteractionToken] = interactionToken
        keys[Route.MessageId] = messageId
        body(MessageEditPatchRequest.serializer(), request)
    }
}
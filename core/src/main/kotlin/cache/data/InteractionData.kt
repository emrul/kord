package dev.kord.core.cache.data

import dev.kord.common.entity.*
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.mapList
import dev.kord.gateway.InteractionCreate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

data class InteractionData(
    val id: Snowflake,
    val type: InteractionType,
    val data: ApplicationCommandInteractionData,
    val guildId: Snowflake,
    val channelId: Snowflake,
    val member: MemberData,
    val token: String,
    val version: Int
) {
    companion object {
        fun from(event: InteractionCreate): InteractionData {
            return with(event.interaction) {
                InteractionData(
                    id,
                    type,
                    ApplicationCommandInteractionData.from(data),
                    guildId,
                    channelId,
                    member.toData(member.user.value!!.id,guildId),
                    token,
                    version
                )
            }
        }
    }
}

@Serializable
data class ApplicationCommandInteractionData(
    val id: Snowflake,
    val name: String,
    val options: Optional<List<OptionData>> = Optional.Missing()
) {
    companion object {
        fun from(data: DiscordApplicationCommandInteractionData): ApplicationCommandInteractionData {
            return with(data) {
                ApplicationCommandInteractionData(
                    id,
                    name,
                    options.mapList { OptionData.from(it) })
            }
        }
    }
}

@Serializable
data class OptionData(
    val name: String,
    val value: Optional<OptionValue<@Serializable(with = NotSerializable::class) Any?>> = Optional.Missing(),
    val subCommand: Optional<List<SubCommand>> = Optional.Missing(),
    val groups: Optional<List<CommandGroup>> = Optional.Missing()
) {
    companion object {
        fun from(data: Option): OptionData {
            return with(data) {
                OptionData(name, value, subCommands, groups)
            }
        }
    }
}


internal object NotSerializable : KSerializer<Any?> {
    override fun deserialize(decoder: Decoder) = TODO("Not yet implemented")
    override val descriptor: SerialDescriptor = String.serializer().descriptor
    override fun serialize(encoder: Encoder, value: Any?) = TODO("Not yet implemented")
}

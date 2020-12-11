package dev.kord.core.behavior

import dev.kord.common.entity.Snowflake
import dev.kord.common.exception.RequestException
import dev.kord.core.Kord
import dev.kord.core.cache.data.RoleData
import dev.kord.core.entity.Entity
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import dev.kord.core.entity.Strategizable
import dev.kord.core.exception.EntityNotFoundException
import dev.kord.core.indexOfFirstOrNull
import dev.kord.core.sorted
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.role.RoleModifyBuilder
import dev.kord.rest.request.RestRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * The behavior of a [Discord Role](https://discord.com/developers/docs/topics/permissions#role-object) associated to a [guild].
 */
interface RoleBehavior : Entity, Strategizable {
    /**
     * The id of the guild this channel is associated to.
     */
    val guildId: Snowflake

    /**
     * The guild behavior this channel is associated to.
     */
    val guild: GuildBehavior get() = GuildBehavior(guildId, kord)

    /**
     * The raw mention of this entity.
     */
    val mention: String
        get() {
            return if (guildId == id) "@everyone"
            else "<@&${id.asString}>"
        }

    /**
     * Requests to change the [position] of this role.
     *
     * This request will execute regardless of the consumption of the return value.
     *
     * @return The roles in of this [guild] in updated order.
     *
     * @throws [RestRequestException] if something went wrong during the request.
     */
    suspend fun changePosition(position: Int): Flow<Role> {
        val response = kord.rest.guild.modifyGuildRolePosition(guildId) {
            move(id to position)
        }
        return response.asFlow().map { RoleData.from(guildId, it) }.map { Role(it, kord) }.sorted()
    }

    /**
     * Requests to get the position of this role in the role list of this guild.
     *
     * @throws [RestRequestException] if something went wrong during the request.
     */
    suspend fun getPosition(): Int = supplier.getGuildRoles(guildId).sorted().indexOfFirstOrNull { it.id == id }!!

    /**
     * Requests to delete this role.
     *
     * @throws [RestRequestException] if something went wrong during the request.
     */
    suspend fun delete() {
        kord.rest.guild.deleteGuildRole(guildId = guildId, roleId = id)
    }

    /**
     * Returns a new [RoleBehavior] with the given [strategy].
     */
    override fun withStrategy(strategy: EntitySupplyStrategy<*>): RoleBehavior = RoleBehavior(guildId, id, kord, strategy)

    companion object {
        internal operator fun invoke(guildId: Snowflake, id: Snowflake, kord: Kord, strategy: EntitySupplyStrategy<*> = kord.resources.defaultStrategy): RoleBehavior = object : RoleBehavior {
            override val guildId: Snowflake = guildId
            override val id: Snowflake = id
            override val kord: Kord = kord
            override val supplier: EntitySupplier = strategy.supply(kord)

            override fun hashCode(): Int = Objects.hash(id, guildId)

            override fun equals(other: Any?): Boolean = when (other) {
                is RoleBehavior -> other.id == id && other.guildId == guildId
                else -> false
            }

            override fun toString(): String {
                return "RoleBehavior(id=$id, guildId=$guildId, kord=$kord, "
            }

        }
    }
}

/**
 * Requests to edit this role.
 *
 * @return The edited [Role].
 *
 * @throws [RestRequestException] if something went wrong during the request.
 */
@OptIn(ExperimentalContracts::class)
suspend inline fun RoleBehavior.edit(builder: RoleModifyBuilder.() -> Unit): Role {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    val response = kord.rest.guild.modifyGuildRole(guildId = guildId, roleId = id, builder = builder)
    val data = RoleData.from(id, response)

    return Role(data, kord)
}

/**
 * Requests to get the this behavior as a [Role].
 *
 * @throws [RequestException] if anything went wrong during the request.
 * @throws [EntityNotFoundException] if the role wasn't present.
 */
suspend fun RoleBehavior.asRole() = supplier.getRole(guildId, id)

/**
 * Requests to get this behavior as a [Role],
 * returns null if the role wasn't present.
 *
 * @throws [RequestException] if anything went wrong during the request.
 */
suspend fun RoleBehavior.asRoleOrNull() = supplier.getRoleOrNull(guildId, id)
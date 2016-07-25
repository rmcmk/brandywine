package plugin.command

import me.ryleykimmel.brandywine.game.command.CommandEvent
import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.model.EntityType
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.model.skill.Skill
import me.ryleykimmel.brandywine.game.model.skill.SkillUtil
import plugin.message
import plugin.surrounding
import plugin.teleport

@Consumes(CommandEvent::class)
class CommandEventConsumer() : EventConsumer<CommandEvent> {

    override fun accept(event: CommandEvent) {
        val args = event.arguments

        with(event.player) {
            when (event.name) {
                "pos" -> message("You are standing at: %s", position)

                "print-surrounding" -> surrounding<Player>(EntityType.PLAYER).forEach { message("Player in your region: %s", it.username) }

                "master" -> {
                    skills.execute { skills.addExpeirence(it.id, SkillUtil.experienceOf(Skill.MAXIMUM_LEVEL)) }
                    message("You are now a master of all skills.")
                }

                "add-experience" -> {
                    if (!args.hasRemaining(2)) {
                        message(
                                "There are 2 required arguments: ::add-experience [skill-id, experience]")
                        return
                    }

                    try {
                        val id = args.nextInteger
                        val experience = args.nextDouble

                        skills.addExpeirence(id, experience)
                    } catch (cause: NumberFormatException) {
                        message("The arguments for this command may only be numeric.")
                    }
                }

                "tele-to" -> {
                    if (!args.hasRemaining(2)) {
                        message(
                                "There are 2 required arguments: ::tele-to [x, y, optional-height]")
                        return
                    }

                    try {
                        val x = args.nextInteger
                        val y = args.nextInteger
                        val height = if (args.hasRemaining(1)) args.nextInteger else position.height

                        teleport(x, y, height)
                    } catch (cause: NumberFormatException) {
                        message("The arguments for this command may only be numeric.")
                    }
                }
            }
        }
    }

}

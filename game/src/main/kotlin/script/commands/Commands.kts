//package script.commands
//
//import me.ryleykimmel.brandywine.game.command.CommandEvent
//import me.ryleykimmel.brandywine.game.model.EntityType
//import me.ryleykimmel.brandywine.game.model.player.Player
//import me.ryleykimmel.brandywine.game.model.skill.Skill
//import me.ryleykimmel.brandywine.game.model.skill.SkillUtil
//import script.KotlinScript.on
//import script.message
//import script.surrounding
//import script.teleport
//import java.lang.NumberFormatException
//
//on { CommandEvent::class }.then {
//    when (name) {
//        "pos" -> player.message("You are standing at: %s", player.position)
//
//        "print-surrounding" -> player.surrounding<Player>(EntityType.PLAYER).forEach { player.message("Player in your region: %s", it.username) }
//
//        "master" -> {
//            player.skills.execute { player.skills.addExpeirence(it.id, SkillUtil.experienceOf(Skill.MAXIMUM_LEVEL)) }
//            player.message("You are now a master of all skills.")
//        }
//
//        "add-experience" -> {
//            if (!arguments.hasRemaining(2)) {
//                player.message(
//                        "There are 2 required arguments: ::add-experience [skill-id, experience]")
//                return@then
//            }
//
//            try {
//                val id = arguments.nextInteger
//                val experience = arguments.nextDouble
//
//                player.skills.addExpeirence(id, experience)
//            } catch (cause: NumberFormatException) {
//                player.message("The arguments for this command may only be numeric.")
//            }
//        }
//
//        "tele-to" -> {
//            if (!arguments.hasRemaining(2)) {
//                player.message(
//                        "There are 2 required arguments: ::tele-to [x, y, optional-height]")
//                return@then
//            }
//
//            try {
//                val x = arguments.nextInteger
//                val y = arguments.nextInteger
//                val height = if (arguments.hasRemaining(1)) arguments.nextInteger else player.position.height
//
//                player.teleport(x, y, height)
//            } catch (cause: NumberFormatException) {
//                player.message("The arguments for this command may only be numeric.")
//            }
//        }
//    }
//}

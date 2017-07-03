package commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class RoleCommands extends Commands  implements CommandExecutor {

    private IGuild guild;

    @Command(aliases = { "subscribe" })
    public String addRoleCommand(IGuild guild, IChannel channel, IUser user, IMessage message, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        if (!canStart(channel)) {
            return null;
        }
        this.guild = guild;

        String rolesSuccessfullyModified = "";

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {



            if (roles.length >= 1) {
                for (String mRole : roles) {
                    String role = mRole.toLowerCase();

                    rolesSuccessfullyModified = parseRoles(false, role, user);


                }
            } else {
                end(channel);
                return "Please specify a role to subscribe to.";
            }


            if (!rolesSuccessfullyModified.equals("")) {

                end(channel);
                return "Successfully subscribed to:" + rolesSuccessfullyModified;
            } else {
                end(channel);
                return "Please enter a valid role name from the list.";
            }

        } else {
            end(channel);
            return "You must be a verified member!";
        }


    }

    @Command(aliases = { "unsubscribe" })
    public String removeRoleCommand(IGuild guild, IChannel channel, IUser user, IMessage message, String[] roles) throws RateLimitException, DiscordException, MissingPermissionsException {

        if (!canStart(channel)) {
            return null;
        }

        this.guild = guild;

        String rolesSuccessfullyModified = "";

        // Making sure user is verified
        if (user.getRolesForGuild(guild).contains(getRole("Verified Member"))) {



            if (roles.length >= 1) {
                for (String mRole : roles) {
                    String role = mRole.toLowerCase();

                    rolesSuccessfullyModified = parseRoles(true, role, user);


                }
            } else {
                end(channel);
                return "Please specify a role to unsubscribe from.";
            }


            if (!rolesSuccessfullyModified.equals("")) {

                end(channel);
                return "Successfully unsubscribed from:" + rolesSuccessfullyModified;
            } else {
                end(channel);
                return "Please enter a valid role name from the list.";
            }

        } else {
            end(channel);
            return "You must be a verified member!";
        }
    }

    private IRole getRole(String roleName) {
        for (IRole iRole : guild.getRoles()) {
            if (iRole.getName().equalsIgnoreCase(roleName)) {
                return iRole;
            }
        }
        return null;
    }

    private String parseRoles(Boolean deleteRole, String role, IUser user) {

        //String[] roles = ["Java", "Swift", "JavaScript", "C++", "C#", "Python", "Web", "VCS", "Kotlin", "Security"];
        List<IRole> roleList = guild.getRoles();
        String rolesSucessfullyModified = "";

        //remove roles from the list if they dont begin with the * character
       // roleList.removeIf(m -> !(m.getName().substring(0,1) == "*"));

        //for every role on the server

        for (IRole aRoleList : roleList) {

            //if first character of role name begins with *,
            if (aRoleList.getName().substring(0, 1).equals("*")) {
                //if role name  matches user input, add or delete the role from the user and append to the success message
                if (aRoleList.getName().toLowerCase().substring(1).equals(role)) {

                    if (deleteRole) {RequestBuffer.request(() -> user.removeRole(aRoleList));}
                    else RequestBuffer.request(() -> user.addRole(aRoleList));

                    rolesSucessfullyModified = rolesSucessfullyModified + " **" + aRoleList.getName().substring(1) + "**";

                }
            }
        }
        return rolesSucessfullyModified;
    }
}
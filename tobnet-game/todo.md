

## Player Displays
Could some display components be "shared" rather than just for an individual user? Could their display manager still
manage when is what displayed for each player, but the actual object could represent either one player or a group of
them?

For example, all counselors/survivors, should they share the same scoreboard/boss bar, is there any performance gain from
sharing rather than having their own? Well some, as you have to iterate through every player's personal display to find
the component which controls that area and update them 1 by 1. Could be possible area for improvement, sharable ones.
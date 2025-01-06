# ChatColorFork
Plugin for Spigot that allows you to create patterns and colors for the chat

Original plugin: https://www.spigotmc.org/resources/chatcolor-%E2%9C%A8-custom-patterns-%E2%9C%85-full-configurable.93186/

##
What is different from the original plugin?
- Removed the ChatListener forcing messages to be automatically colored
- Added compatibility with Kyori Adventure
- Added placeholder %chatcolor_kyori_pattern%

##
How to add minimessage formatting? https://docs.advntr.dev/minimessage/format.html
- Example (Classic Color)
```yaml
dark_red:
  permission: 'chatcolor.color.dark_red'
  mode: 'SINGLE'
  colors:
    - '4'
  kyori: '<red>'
```
- Example (Hex Color)
```yaml
  icy_blue:
  permission: 'chatcolor.color.icy_blue'
  mode: 'SINGLE'
  colors:
    - '#C6E2FF'
  kyori: '<#C6E2FF>'
```
- Example (Gradient)
```yaml
rainbow_gradient:
  permission: 'chatcolor.color.rainbow_gradient'
  mode: 'GRADIENT'
  colors:
    - '#FF0000'
    - '#F4FF00'
    - '#00FF13'
    - '#00F2FF'
    - '#1200FF'
    - '#FF00FB'
  kyori: '<gradient:#FF0000:#F4FF00:#00FF13:#00F2FF:#1200FF:#FF00FB>'
```

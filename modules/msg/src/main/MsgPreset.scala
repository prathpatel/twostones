package lila.msg

object MsgPreset:

  import lila.core.msg.MsgPreset as Msg

  def maxFollow(username: UserName, max: Max) =
    Msg(
      name = "Follow limit reached!",
      text = s"""Sorry, you can't follow more than $max players on TwoStones.
To follow new players, you must first unfollow some on your profile ($username), under "Following".

Thank you for your understanding.""",
      mustRead = false
    )

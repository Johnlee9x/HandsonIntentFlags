# HandsonIntentFlags

An Android demo app to visually explore how **Intent flags** control Activity creation, reuse, and back stack behavior.

## What You'll Learn

| Flag | Behavior |
|------|----------|
| *(no flag)* | Always creates a **new instance**, pushed on top of stack |
| `FLAG_ACTIVITY_SINGLE_TOP` | Reuses the Activity if it's already at the **top** of the stack (`onNewIntent()`) |
| `FLAG_ACTIVITY_NEW_TASK` | Launches in a **new task** (if different `taskAffinity`) |
| `FLAG_ACTIVITY_CLEAR_TOP` | Destroys everything **above** the target, then **re-creates** it |
| `CLEAR_TOP` + `SINGLE_TOP` | Destroys everything above, **reuses** existing instance (`onNewIntent()`) |
| `NEW_TASK` + `CLEAR_TASK` | **Clears the entire task** and creates a fresh root Activity |
| `FLAG_ACTIVITY_NO_HISTORY` | Activity is **not kept** in the back stack after the user leaves it |
| `FLAG_ACTIVITY_REORDER_TO_FRONT` | **Moves** an existing Activity to the top without destroying others |

## Project Structure

```
MainActivity          — Hub with buttons for each flag demo
BaseChildActivity     — Base class showing taskId, hashCode, lifecycle log
ActivityA / B / C     — Color-coded child Activities (Blue / Green / Orange)
```

## How to Use

1. **Build & Run** on a device or emulator
2. **MainActivity** shows grouped buttons for each flag category
3. Tap **"Start chain: A → B → C"** to build a deep stack (`Main → A → B → C`)
4. From any child Activity, test different flags and observe:
   - `hashCode` / `instanceId` — same = reused, different = new instance
   - Lifecycle log — `onCreate` vs `onNewIntent`
   - Press **Back** repeatedly to see which Activities remain in the stack
5. Open **Logcat** and filter by tag `IntentFlags` for real-time lifecycle events

## Tech Stack

- Kotlin
- AndroidX AppCompat
- Min SDK 29 / Target SDK 36

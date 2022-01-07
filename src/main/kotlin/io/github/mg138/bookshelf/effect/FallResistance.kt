package io.github.mg138.bookshelf.effect

/*
@Component
class FallResistance : FlagEffect() {
    override val identifier = "FALL_RESISTANCE"

    class ActiveFallResistance(
        effect: Effect,
        property: EffectProperty,
        effectManager: EffectManager
    ) : ActiveFlagEffect(effect, property, effectManager), DamageModifier {

        override fun tick() = this.deactivate()
        override fun onDamage(event: EntityDamageEvent) {
            if (event.cause == EntityDamageEvent.DamageCause.FALL) {
                event.isCancelled = true

                DamageHandler.simpleDamage(
                    event.entity,
                    StatUtil.damageMod(
                        event.finalDamage, max((1 - this.property.power), 0.0)
                    )
                )
            }
        }
    }

    override fun makeFlagEffect(effect: Effect, property: EffectProperty, effectManager: EffectManager) =
        ActiveFallResistance(effect, property, effectManager)
}
 */
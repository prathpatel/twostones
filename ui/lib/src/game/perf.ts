// Go has no variants: a rating is a speed, or "standard" for the overall rating
export const perfName = (perf: Speed | VariantKey): string =>
  perf === 'standard' ? i18n.variant.standard : i18n.site[perf];

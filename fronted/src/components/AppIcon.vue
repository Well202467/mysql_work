<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: {
    type: String,
    required: true,
  },
  size: {
    type: [Number, String],
    default: 20,
  },
  stroke: {
    type: Number,
    default: 1.8,
  },
})

const iconName = computed(() => String(props.name || '').trim())
</script>

<template>
  <svg
    class="app-icon"
    :width="size"
    :height="size"
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
  >
    <template v-if="iconName === 'home'">
      <path d="M4 10.5L12 4l8 6.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M6.5 9.5V19h11V9.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M10 19v-5h4v5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
    </template>

    <template v-else-if="iconName === 'majors' || iconName === 'major'">
      <path d="M4.5 6.5C4.5 5.4 5.4 4.5 6.5 4.5H10.5C11.5 4.5 12.4 5.1 12.8 6" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M19.5 6.5C19.5 5.4 18.6 4.5 17.5 4.5H13.5C12.5 4.5 11.6 5.1 11.2 6" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M5 6.5h5.5c.9 0 1.7.4 2.2 1.1.5-.7 1.3-1.1 2.2-1.1H20v11.2c0 .99-.81 1.8-1.8 1.8h-4.7c-.93 0-1.8.37-2.46 1.04L12 21l.96-.46A3.48 3.48 0 0 1 14.42 20H19c.55 0 1-.45 1-1V6.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M5 6.5V19c0 .55.45 1 1 1h4.58c.52 0 1.02.12 1.46.35L12 20.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
    </template>

    <template v-else-if="iconName === 'map'">
      <circle cx="5.5" cy="6.5" r="2" :stroke-width="stroke" stroke="currentColor" />
      <circle cx="18.5" cy="8.5" r="2" :stroke-width="stroke" stroke="currentColor" />
      <circle cx="8.5" cy="18" r="2" :stroke-width="stroke" stroke="currentColor" />
      <circle cx="18" cy="17.5" r="2" :stroke-width="stroke" stroke="currentColor" />
      <path d="M7.3 7.2l9.4 1.6" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M6.8 8.2l1.4 7.6" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M10.4 17.8l5.6-.3" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M17.8 10.6l.2 4.9" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
    </template>

    <template v-else-if="iconName === 'resources' || iconName === 'support'">
      <path d="M7 4.5h9.5A2.5 2.5 0 0 1 19 7v12.5H7.8A2.8 2.8 0 0 0 5 22V7A2.5 2.5 0 0 1 7.5 4.5Z" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M7.5 4.5A2.5 2.5 0 0 0 5 7v12.2" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8.5 9h7" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8.5 12.5h7" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8.5 16h4.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
    </template>

    <template v-else-if="iconName === 'course'">
      <rect x="5" y="4.5" width="14" height="15" rx="2.2" :stroke-width="stroke" stroke="currentColor" />
      <path d="M8.5 8h7" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8.5 11.5h7" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8.5 15h4.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
    </template>

    <template v-else-if="iconName === 'ability'">
      <circle cx="12" cy="12" r="7.5" :stroke-width="stroke" stroke="currentColor" />
      <path d="M12 12l4.6-4.1" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <circle cx="12" cy="12" r="1.5" :stroke-width="stroke" stroke="currentColor" />
      <path d="M12 4.5v2" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M19.5 12h-2" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M12 19.5v-2" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M4.5 12h2" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
    </template>

    <template v-else-if="iconName === 'job'">
      <path d="M8 20V6.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M8 7h9l-2.4 3L17 13H8" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M8 12H4.8l1.4-2L4.8 8H8" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
    </template>

    <template v-else-if="iconName === 'focus'">
      <path d="M12 4.5v3" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M12 16.5v3" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M4.5 12h3" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M16.5 12h3" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <circle cx="12" cy="12" r="4.5" :stroke-width="stroke" stroke="currentColor" />
      <circle cx="12" cy="12" r="1.4" fill="currentColor" />
    </template>

    <template v-else-if="iconName === 'intro'">
      <path d="M6 5.5h12" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M6 9h12" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M6 12.5h8.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <path d="M6 16h6.5" :stroke-width="stroke" stroke="currentColor" stroke-linecap="round" />
      <rect x="4.5" y="3.5" width="15" height="17" rx="2.2" :stroke-width="stroke" stroke="currentColor" />
    </template>
  </svg>
</template>

<style scoped>
.app-icon {
  display: inline-block;
  vertical-align: middle;
  flex: none;
}
</style>

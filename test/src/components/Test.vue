<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {BytedanceAd} from "capacitor-bytedance-csj-plugin-ts";

defineProps<{ msg: string }>()

const count = ref(0)
onMounted(async () => {
    await BytedanceAd.init({
        appId: "5453122",
        appName: "test",
        debug: true,
    })
})


async function showRewardVideo() {
    await BytedanceAd.showRewardVideoAd(
        {
            slotId: "954659343",
            extra: null,
        }
    )
}

</script>

<template>
    <h1>{{ msg }}</h1>

    <div class="card">
        <button type="button" @click="count++">count is {{ count }}</button>
        <button @click="showRewardVideo">Show Reward Video</button>
    </div>

</template>

<style scoped>
.read-the-docs {
    color: #888;
}
</style>

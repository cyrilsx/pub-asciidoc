<template>
    <ProjectSummary/>
</template>

<script lang="ts">
    import { Component, Vue } from 'vue-property-decorator';
    import ProjectSummary from '@/components/ProjectSummary.vue';

    @Component({
        components: {
            ProjectSummary,
        },
    })
    export default class Home extends Vue {
    }
</script>

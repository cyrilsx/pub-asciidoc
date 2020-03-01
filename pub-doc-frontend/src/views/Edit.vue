<template>
    <v-form ref="form" v-model="valid" :lazy-validation="false">
        <p v-if="errors.length">
            <b>Please correct the following error(s):</b>
            <ul>
                <li v-for="error in errors">{{ error }}</li>
            </ul>
        </p>

        <v-text-field v-model="project.projectName" :counter="25" label="Name" required></v-text-field>
        <v-text-field v-model="project.pathToDocumentation" label="Path to documentation" required></v-text-field>
        <v-text-field v-model="project.description" label="Description" required></v-text-field>
        <v-text-field v-model="project.gitRepository" label="Git" required></v-text-field>

        <v-btn color="success" class="mr-4" @click="check">Validate</v-btn>
    </v-form>
</template>

<script lang="ts">
    import { Component, Vue } from 'vue-property-decorator';
    import { ProjectsApi } from '@/api/backend';
    import router from '@/router';

    @Component({
        components: {},
    })
    export default class Edit extends Vue {
        project: Project = {
            projectName: '',
            docsLink: '',
            description: '',
            gitRepository: '',
            pathToDocumentation: '',
        };

        private errors: string[] = [];
        valid = false;

        private update() {
            ProjectsApi.create(this.project)
                .then((r) => router.push('/'));
        }

        private check() {
            this.errors = [];
            if (this.project.projectName.trim().length === 0) {
                this.errors.push('project name can\'t be empty.');
            }

            if (this.project.gitRepository.trim().length === 0) {
                this.errors.push('git repository can\'t be empty.');
            }

            if (this.project.description.trim().length === 0) {
                this.errors.push('description can\'t be empty.');
            }

            this.valid = this.errors.length === 0;
            if (this.valid) {
                this.update();
            }
        }

    }
</script>

<style scoped>
</style>
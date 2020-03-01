<template>
    <div>
        <h3>Projects</h3>
        <v-simple-table v-if="true">
        <!--v-simple-table v-if="projects.length > 0"-->
            <template v-slot:default>
                <thead>
                <tr>
                    <th class="text-left">Name</th>
                    <th class="text-left">Path to docs</th>
                    <th class="text-left">Git</th>
                    <th class="text-left">Link</th>
                    <th class="text-left">Action</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="project in projects" :key="project.name">
                    <td>{{ project.projectName }}</td>
                    <td>{{ project.pathToDocumentation }}</td>
                    <td>{{ project.gitRepository }}</td>
                    <td>{{ project.docsLink }}></td>
                    <td>
                        <v-row justify="space-around">
                            <v-icon>fa-download</v-icon>
                        </v-row>
                    </td>
                </tr>
                </tbody>
            </template>
        </v-simple-table>
        <v-card v-else>
            <v-card-text>Your projects are empty! Why not to create a <a href="/edit-project">new project</a></v-card-text>
        </v-card>
    </div>
</template>

<script lang="ts">
    import { Component, Prop, Vue } from 'vue-property-decorator';
    import { ProjectsApi } from '@/api/backend';

    @Component
    export default class ProjectSummary extends Vue {
        @Prop() private msg!: string;
        projects: Project[] = [];
        private message = '';

        constructor() {
            super();
            ProjectsApi.getAllProjects()
                .then(p => this.projects = p)
                .catch(err => this.message = err);
        }

        private searchByName(items: Project[], term: string) {
            if (term) {
                return items.filter((item) => item.projectName.includes(term));
            }
            return items;
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    h3 {
        color: cadetblue;
        text-align: left;
        padding-left: 10px;
        margin: 10px 40px 10px 0;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }
</style>

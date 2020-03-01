import Axios from 'axios';

export class ProjectsApi {
    public static async getAllProjects(): Promise<Project[]> {
        const url = '/api/project';
        const response = await this.projectAxios.get<Project[]>(url, {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Accept': 'application/json',
            },
        });
        return response.data;
    }

    public static async create(project: Project): Promise<Project> {
        const url = '/api/project';
        const response = await this.projectAxios.post<Project>(url, project);
        return response.data;
    }


    private static projectAxios = Axios.create({
        baseURL: 'http://localhost:8080/',
        timeout: 1000,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            'Accept': 'application/json',
        },
    });

}

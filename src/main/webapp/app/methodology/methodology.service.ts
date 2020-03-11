import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators/map';
import { MethodologyPageContent } from './';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable()
export class MethodologyService {
    private restUrl = SERVER_API_URL + 'api/custom/methodology';

    constructor(private http: HttpClient) {}

    get(): Observable<MethodologyPageContent> {
        return this.http.get<MethodologyPageContent>(this.restUrl);
    }
    save(data): Observable<Object> {
        return this.http.post(this.restUrl, data);
    }
}

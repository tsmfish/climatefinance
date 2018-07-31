import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Integration } from './integration.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Integration>;

@Injectable()
export class IntegrationService {

    private resourceUrl =  SERVER_API_URL + 'api/integrations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/integrations';

    constructor(private http: HttpClient) { }

    create(integration: Integration): Observable<EntityResponseType> {
        const copy = this.convert(integration);
        return this.http.post<Integration>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(integration: Integration): Observable<EntityResponseType> {
        const copy = this.convert(integration);
        return this.http.put<Integration>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Integration>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Integration[]>> {
        const options = createRequestOption(req);
        return this.http.get<Integration[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Integration[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Integration[]>> {
        const options = createRequestOption(req);
        return this.http.get<Integration[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Integration[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Integration = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Integration[]>): HttpResponse<Integration[]> {
        const jsonResponse: Integration[] = res.body;
        const body: Integration[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Integration.
     */
    private convertItemFromServer(integration: Integration): Integration {
        const copy: Integration = Object.assign({}, integration);
        return copy;
    }

    /**
     * Convert a Integration to a JSON which can be sent to the server.
     */
    private convert(integration: Integration): Integration {
        const copy: Integration = Object.assign({}, integration);
        return copy;
    }
}

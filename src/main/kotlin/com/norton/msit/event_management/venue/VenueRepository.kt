package com.norton.msit.event_management.venue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VenueRepository : JpaRepository<Venue, Long> {
}
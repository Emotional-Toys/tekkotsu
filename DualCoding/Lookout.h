//-*-c++-*-
#ifndef INCLUDED_Lookout_h_
#define INCLUDED_Lookout_h_

#include <queue>

#include "Behaviors/BehaviorBase.h"
#include "Motion/MMAccessor.h"
#include "Motion/MotionManager.h"
#include "Motion/HeadPointerMC.h"
#include "Motion/MotionSequenceMC.h"
#include "Motion/PostureMC.h"
#include "Events/VisionObjectEvent.h"
#include "LookoutRequests.h"

namespace DualCoding {


//! The Lookout accepts LookoutRequests to move the head and collect sensor information.
/*!
 HeadMotionType can be none (user will point the head himself),
 pointAt, scan, track, or search.  The data collected can be an image
 or distance reading, or for scan operations, it can be a list of
 locations where certain VisionObject or VisionRegion streams reported
 hits.

*/

class Lookout : public BehaviorBase {
public:
  //! Constructor
  Lookout();

  virtual void DoStart();
  virtual void DoStop();
  virtual void processEvent(const EventBase& event);

  static std::string getClassDescription() { return "Moves head and collects data according to request"; }
  virtual std::string getDescription() const { return getClassDescription(); }

  virtual unsigned int executeRequest(const LookoutRequest&);
  bool busy() { return curReq != NULL; }
  void stopTrack();

  static std::vector<DualCoding::Point> groundSearchPoints(); //!< returns a vector of points for searching the ground around the robot

  void moveHeadToPoint();
  //  bool isLandmarkVisible() const { return landmark_inview; }

  void relax(); //!< Inactivates all Lookout motion commands; called when MapBuilder finishes

  static Point findLocationFor(const float normX, const float normY);
  static const unsigned int invalid_LO_ID=(unsigned int)-1;
  
protected:
  virtual void executeRequest();
  virtual void requestComplete(bool result=true);

  template<class T>
  void pushRequest(const LookoutRequest& req) {
    requests.push(new T(dynamic_cast<const T&>(req)));
  }
    

  //@name PointAtRequest functions
  //@{
  void processPointAtEvent(const EventBase& event);
  bool findPixelModes();
#ifdef TGT_HAS_IR_DISTANCE
  bool findDistanceMode();
  float getDistanceModeValue();
#endif
  //@}

  //@name ScanRequest functions
  //@{
  void setupScan();
  void triggerScanMotionSequence();
  void processScanEvent(const EventBase& event);
  void scanAlongLine(const Point& start,const Point& end);
  void scanAlongPolygon(const std::vector<Point>& vertices, const bool closed=false);
  void scanArea(const Point &topLeft, const Point &topRight,
		const Point &bottomLeft, const Point &bottomRight);

  static Point findLocationFor(const VisionObjectEvent& visev) {
    return findLocationFor(visev.getCenterX(), visev.getCenterY());
  }

  static Point findLocationFor(const CMVision::region* reg) {
    return findLocationFor(2.0*reg->cen_x/VRmixin::camSkS.getWidth()-1.0,
			   2.0*reg->cen_y/VRmixin::camSkS.getHeight()-1.0);
  }

  void storeVisionRegionDataTo(std::vector<Point>&, const std::set<color_index>&, int);
#ifdef TGT_HAS_IR_DISTANCE
  void storeIRDataTo(std::vector<Point>&);
#endif
  //@}

  //@name TrackRequest functions
  //@{
  void setupTrack();
  void processTrackEvent(const EventBase& event);
  /*
  void processTrackVision(float normX, float normY, bool isCurrentlyVisible);
  void trackObjectAt(float normX, float normY); //! tracks point at [normX,normY] in cam frame
  Point findLocationFor(float, float);
  */
  //@}

  //@name SearchRequest functions
  //@{
  void setupSearch();
  void processSearchEvent(const EventBase& event);
  void triggerSearchMotionSequence();
  static void searchAt(HeadPointerMC &hpmc_temp,
		       std::vector<float> &jointvals,
		       const Point &target);
  //@}


private:
  Lookout& operator=(const Lookout&);
  Lookout(const Lookout&);

protected:
  std::vector<std::map<uchar,unsigned int> > pixelHistograms;
  std::priority_queue<float> distanceSamples;

  MotionManager::MC_ID pointer_id;  //!< id for HeadPointerMC for pointing the camera
  MotionManager::MC_ID posture_id;  //!< id for PostureMC for pointing the IR sensors
  MotionManager::MC_ID sequence_id; //!< id for MotionSequenceMC for scanning
  std::queue<LookoutRequest*> requests;  //!< queue of pending LookoutRequest instances, including the current request
  LookoutRequest *curReq;           //!< pointer to request currently being executed
  LookoutPointRequest *curPAR;      //!< current Point-At request (same object as curReq)
  bool successSave;

  enum TrackerStates {
    inactive,
    moveToAcquire,
    tracking,
    searching,
    centering,
    lost
  } trackerState;
  unsigned int idCounter;

  enum LookoutTimerSourceId_t {
    settle_timer = 1,
    sample_timer,
    lost_timer,
    scan_timer, //scan_timer has to be at the end of enum list
  };
};

} //namespace

#endif
